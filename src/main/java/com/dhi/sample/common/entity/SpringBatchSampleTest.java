package com.dhi.sample.common.entity;

import lombok.Getter;
import lombok.Setter;

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
@Setter
@Table(name = "spring_batch_sample_test")
public class SpringBatchSampleTest {

	@Id
	private Long seq;
	private Long pam_mapping_seq;
	private Long asset_seq;
	private Long model_seq;
	private String active_yn;
	private String used_yn;
	private String comments;

	public SpringBatchSampleTest(
			Long pam_mapping_seq,
			Long asset_seq,
			Long model_seq,
			String active_yn,
			String used_yn,
			String comments) {
		this.pam_mapping_seq = pam_mapping_seq;
		this.asset_seq = asset_seq;
		this.model_seq = model_seq;
		this.active_yn = active_yn;
		this.used_yn = used_yn;
		this.comments = comments;
	}
}
