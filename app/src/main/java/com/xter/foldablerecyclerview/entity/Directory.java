package com.xter.foldablerecyclerview.entity;

import java.util.Objects;

public class Directory {

	public String name;
	public int size;

	public Directory(String name, int size) {
		this.name = name;
		this.size = size;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Directory directory = (Directory) o;
		return Objects.equals(name, directory.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Directory{" +
				"name='" + name + '\'' +
				", size=" + size +
				'}';
	}
}
