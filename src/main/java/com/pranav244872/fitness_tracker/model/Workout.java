package com.pranav244872.fitness_tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="workouts")
public class Workout {
	///////////////////////////////////////////////////////////////////////////
	/// Fields
	///////////////////////////////////////////////////////////////////////////
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int targetSets;

    @Column(nullable = false)
    private int targetReps;

    @Column(nullable = false)
    private int restBetweenSetsSeconds;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

	///////////////////////////////////////////////////////////////////////////
	/// Constructors
	///////////////////////////////////////////////////////////////////////////
	public Workout() {
	}

	public Workout(Long id, String name, int targetSets, int targetReps, int restBetweenSetsSeconds,
			Category category) {
		this.id = id;
		this.name = name;
		this.targetSets = targetSets;
		this.targetReps = targetReps;
		this.restBetweenSetsSeconds = restBetweenSetsSeconds;
		this.category = category;
	}

	///////////////////////////////////////////////////////////////////////////
	/// Getters and Setters
	///////////////////////////////////////////////////////////////////////////
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTargetSets() {
		return targetSets;
	}

	public void setTargetSets(int targetSets) {
		this.targetSets = targetSets;
	}

	public int getTargetReps() {
		return targetReps;
	}

	public void setTargetReps(int targetReps) {
		this.targetReps = targetReps;
	}

	public int getRestBetweenSetsSeconds() {
		return restBetweenSetsSeconds;
	}

	public void setRestBetweenSetsSeconds(int restBetweenSetsSeconds) {
		this.restBetweenSetsSeconds = restBetweenSetsSeconds;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
