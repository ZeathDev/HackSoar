package dev.hacksoar.api.events.impl;

import dev.hacksoar.api.events.Event;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;

public class EventRenderChunkPosition extends Event{

	private RenderChunk renderChunk;
	private BlockPos blockPos;
	
	public EventRenderChunkPosition(RenderChunk renderChunk, BlockPos blockPos) {
		this.renderChunk = renderChunk;
		this.blockPos = blockPos;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public void setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	public RenderChunk getRenderChunk() {
		return renderChunk;
	}
}
