package com.soundlooper.gui.customComponent.playerView;

public class SoundLooperPlayerDragEvent {
	private double startPx;
	private boolean drag = false;
	private double dragOffset;

	public void startDrag(double startPx, double dragOffset) {
		this.startPx = startPx;
		this.dragOffset = dragOffset;
		this.drag = true;
	}

	public void endDrag() {
		this.startPx = 0;
		this.dragOffset = 0;
		this.drag = false;
	}

	public boolean isDrag() {
		return drag;
	}

	public boolean isNotDrag() {
		return !isDrag();
	}

	public void setDrag(boolean drag) {
		this.drag = drag;
	}

	public double getStartPx() {
		return startPx;
	}

	public void setStartPx(double startPx) {
		this.startPx = startPx;
	}

	public double getDragOffset() {
		return dragOffset;
	}

	public void setDragOffset(double dragOffset) {
		this.dragOffset = dragOffset;
	}
}
