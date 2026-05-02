package com.pranav244872.fitness_tracker.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class WorkoutLog {
	///////////////////////////////////////////////////////////////////////////
	/// Fields
	///////////////////////////////////////////////////////////////////////////
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime completionDate;

	@Column(nullable = false)
	private int durationMinutes;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	///////////////////////////////////////////////////////////////////////////
	/// Constructors
	///////////////////////////////////////////////////////////////////////////
	public WorkoutLog(Long id, LocalDateTime completionDate, int durationMinutes, Category category) {
		this.id = id;
		this.completionDate = completionDate;
		this.durationMinutes = durationMinutes;
		this.category = category;
	}

	public WorkoutLog() {
	}

	///////////////////////////////////////////////////////////////////////////
	/// Getters and Setters
	///////////////////////////////////////////////////////////////////////////
	public int getDurationMinutes() {
		return durationMinutes;
	}

	public void setDurationMinutes(int durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(LocalDateTime completionDate) {
		this.completionDate = completionDate;
	}
}
