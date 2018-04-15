package br.unipe.posweb.modelo;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable{

	private static final long serialVersionUID = 431691507225158896L;

	private String remetente;
	
	private String destinatario;
	
	private String mensagem;
	
	private Date dataHoraEnvio;

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getDataHoraEnvio() {
		return dataHoraEnvio;
	}

	public void setDataHoraEnvio(Date dataHoraEnvio) {
		this.dataHoraEnvio = dataHoraEnvio;
	}
	
}
