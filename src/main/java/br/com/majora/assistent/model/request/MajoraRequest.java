package br.com.majora.assistent.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MajoraRequest {
	private String model;
	private List<Messages> messages;
	private boolean think = true;
	private boolean stream = false;
}
