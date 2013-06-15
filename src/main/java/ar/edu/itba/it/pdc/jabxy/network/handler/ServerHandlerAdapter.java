package ar.edu.itba.it.pdc.jabxy.network.handler;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import ar.edu.itba.it.pdc.jabxy.network.dispatcher.Dispatcher;
import ar.edu.itba.it.pdc.jabxy.network.queues.InputQueue;
import ar.edu.itba.it.pdc.jabxy.network.queues.OutputQueue;

public class ServerHandlerAdapter extends AbstractHandlerAdapter {

	private final Object stateChangeLock = new Object();
	private boolean shuttingDown = false;

	public ServerHandlerAdapter(Dispatcher dispatcher, InputQueue inputQueue, OutputQueue outputQueue, EventHandler clientHandler) {
		super(dispatcher, inputQueue, outputQueue, clientHandler);
	}

	// ------------------------------------------------------------
	// Implementation of Callable<HandlerAdapter> interface

	/* (non-Javadoc)
	 * @see ar.edu.itba.it.pdc.jabxy.network.handler.HandlerAdapter#call()
	 */
	@Override
	public HandlerAdapter call() throws IOException {
		try {
			drainOutput();
			fillInput();

			ByteBuffer message;

			// must process all buffered messages because Selector will
			// not fire again for input that's already read and buffered
			while ((message = getHandler().nextMessage(this)) != null) {
				getHandler().handleInput(message, this);
			}
		} finally {
			synchronized (stateChangeLock) {
				setRunning(false);
			}
		}

		return this;
	}

	// --------------------------------------------------
	// Private helper methods

	// These three methods manipulate the private copy of the selection
	// interest flags. Upon completion, this local copy will be copied
	// back to the SelectionKey as the new interest set.
	private void enableWriteSelection() {
		modifyInterestOps(SelectionKey.OP_WRITE, 0);
	}

	private void disableWriteSelection() {
		modifyInterestOps(0, SelectionKey.OP_WRITE);
	}

	private void disableReadSelection() {
		modifyInterestOps(0, SelectionKey.OP_READ);
	}

	// If there is output queued, and the channel is ready to
	// accept data, send as much as it will take.
	private void drainOutput() throws IOException {
		if (((getReadyOps() & SelectionKey.OP_WRITE) != 0)
				&& (!outputQueue().isEmpty())) {
			outputQueue().drainTo((ByteChannel) getChannel());
		}

		// Write selection is turned on when output data in enqueued,
		// turn it off when the queue becomes empty.
		if (outputQueue().isEmpty()) {
			disableWriteSelection();

			if (shuttingDown) {
				getChannel().close();
				getHandler().stopped(this);
			}
		}
	}

	// Attempt to fill the input queue with as much data as the channel
	// can provide right now. If end-of-stream is reached, stop read
	// selection and shutdown the input side of the channel.
	private void fillInput() throws IOException {
		if (shuttingDown)
			return;

		int rc = inputQueue().fillFrom((ByteChannel) getChannel());

		if (rc == -1) {
			disableReadSelection();

			if (getChannel() instanceof SocketChannel) {
				SocketChannel sc = (SocketChannel) getChannel();

				if (sc.socket().isConnected()) {
					try {
						sc.socket().shutdownInput();
					} catch (SocketException e) {
						// happens sometimes, ignore
					}
				}
			}

			shuttingDown = true;
			getHandler().stopping(this);

			// cause drainOutput to run, which will close
			// the socket if/when the output queue is empty
			enableWriteSelection();
		}
	}

}
