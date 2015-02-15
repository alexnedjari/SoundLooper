package com.soundlooper.gui;

import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.soundlooper.model.song.Song;

public class FavoriteTreeNode implements MutableTreeNode{
	
	private Song song;

	public FavoriteTreeNode(Song song) {
		super();
		this.song = song;
	}

	@Override
	public Enumeration children() {
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public int getIndex(TreeNode node) {
		return 0;
	}

	@Override
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(MutableTreeNode child, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(MutableTreeNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserObject(Object object) {
		// TODO Auto-generated method stub
		
	}

}
