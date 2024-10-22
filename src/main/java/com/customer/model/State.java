package com.customer.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "states")
@Data @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

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
    
    
}
