package com.example.demo.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Book {
    /**
     * Google Books API id.
     */
    @Id
    private String id;
    private String title;
    private String author;
    private Integer pageCount;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	
    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    
    protected Book() {
        
    }
}

