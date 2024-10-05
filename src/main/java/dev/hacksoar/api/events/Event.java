package dev.hacksoar.api.events;

import dev.hacksoar.HackSoar;

import java.lang.reflect.InvocationTargetException;

public abstract class Event {

	private boolean cancelled;

	public enum State {
		PRE("PRE", 0), POST("POST", 1);
		private State(String string, int number) {
		}
	}

	public Event call() {
		this.cancelled = false;
		Event.call(this);
		return this;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {

		this.cancelled = cancelled;
	}

	private static void call(Event event) {
		ArrayHelper<Data> dataList = HackSoar.instance.eventManager.get(event.getClass());
		if (dataList != null) {
			for (Data data : dataList) {
				try {
					data.target.invoke(data.source, event);
				} catch (IllegalAccessException e) {
					// Don't give crackers clues...
					if (HackSoar.instance.DEVELOPMENT_SWITCH)
						e.printStackTrace();
				} catch (InvocationTargetException e) {
					// Don't give crackers clues...
					if (HackSoar.instance.DEVELOPMENT_SWITCH)
						e.printStackTrace();
				}

			}
		}
	}
}
