package com.xter.foldablerecyclerview.entity;

import java.util.Objects;

public class File {

	public String name;
	public int size;

	public File(String name, int size) {
		this.name = name;
		this.size = size;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		File file = (File) o;
		return size == file.size &&
				Objects.equals(name, file.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, size);
	}

	@Override
	public String toString() {
		return "File{" +
				"name='" + name + '\'' +
				", size=" + size +
				'}';
	}
}
