package com.tri.ui.test.domain.model.sample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Sample implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private Long version;

	@NotNull
	@Size(max = 30)
	@Column(name = "NAME", length = 30)
	private String name;

	@NotNull
	@Min(0)
	@Column(name = "AGE")
	private Long age;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Sample.id = " + id;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		} else if (other == null || getClass() != other.getClass()) {
			return false;
		}

		Sample castOther = (Sample) other;
		if (id != null) {
			return id.equals(castOther.id);
		} else if (castOther.id != null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		} else {
			return 0;
		}
	}

}
