package com.dhi.sample.common.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * pam_mapping Entity
 *
 * @author SungTae, Kang
 */
@Entity
@Getter
@Table(name = "pam_mapping")
public class PamMapping {

	@Id private Long pam_mapping_seq;
	private Long asset_seq;
	private Long model_seq;
	private String active_yn;
	private String used_yn;
	private Long data_import_interval;
}
