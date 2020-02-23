package com.dhi.sample.common.config;

import org.springframework.batch.core.JobExecution;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Prevision batch 상수
 *
 * @author SungTae, Kang
 */
public class Constants {

	/* prevision batch */
	public static final int PAGE_SIZE =10000;                                       // select 시 limit 단위 (사용 할 시 order by를 꼭 해줘야 함)
	public static final int CHUNK_SIZE = 10000;                                     // 처리 단위
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");  // date 포맷
	/* batch stop 을 위해 job id 관리 */
	public static Map<String, JobExecution> jobExecutionMap = new HashMap();
	/* calculation */
	public static final String UNISTEAM97_FILE_NAME = "UniSteam97_JNI";     		// uniSteam file path
	/* alarms status */
	public static final Long ALARM_STATUS_NEW = 14L;                                // 신규 알람
	public static final Long ALARM_STATUS_FINISHED = 16L;                           // action 처리 완료
	/* incident type */
	public static final String WINDOW = "window";                                   // window       :   tag_data_raw 에서 total 갯수 만큼의 data 중에서 임계치를 넘는 수가 target 수 이상 일 때 alarm 발생
	public static final String STEP = "step";                                       // step         :   tag_data_raw 에서 total 갯수 만큼의 data 평균 수치 를 뺀 값이 임계치를 넘을 때 alarm 발생
	public static final String CONDITION = "condition";                             // condition    :   tag_data_raw 에서 data 를 뺀 값이 수치가 임계치를 넘을 때 alarm 발생
	/* incident comparison */
	public static final String GREATER = "greater";                                 // value > targetValue;
	public static final String GREATER_EQUAL = "greaterEqual";                      // value >= targetValue;
	public static final String EQUAL = "equal";                                     // value.equals(targetValue);
	public static final String LESSER_EQUAL = "lesserEqual";                        // value <= targetValue;
	public static final String LESSER = "lesser";                                   // value < targetValue;
	/* Threshold */
	public static final String AH = "AH";                                           // Threshold_actual_high
	public static final String AL = "AL";                                           // Threshold_actual_low
	public static final String RP = "RP";                                           // Threshold_residual_high
	public static final String RN = "RN";                                           // Threshold_residual_low
	public static final String RESIDUAL = "residual";                               // residual
	/* message */
	public static final String REFERENCE_DATA_DOES_NOT_EXIST = "reference data does not exist";
	public static final String UNEXPECTED_ERROR = "Unexpected Error";
	public static final String NO_DATA_MSG = "Data does not exist";
	public static final String CALCULATION_EXPRESSION_PASING_ERROR = "Calculation Expression Pasing error";
	public static final String NO_CURRENT_DATA_MSG = "Data does not exist";
	public static final String NOT_A_NUMBER = "Not a number (\"NA\")";
	public static final String IS_NOT_SETTED_MSG = " is not setted in ";
	public static final String COMPARISON_IS_NOT_SETTED_MSG = "Comparison is not setted in pam_tag_mappping";
	public static final String OUT_OF_THRESHOLD_MSG = "Out Of Threshold";
	public static final String THERE_ARE_NO_VALUES_WITHIN_THE_THRESHOLD = "There are no values within the threshold";
	public static final String FIRST_ACTION_MESSAGE = "최초발생";
	/* thread poll */
	public static final int FORK_JOIN_POOL_SIZE = 8;                               // 병렬 처리 thread 수
}