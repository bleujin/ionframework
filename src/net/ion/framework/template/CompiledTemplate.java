package net.ion.framework.template;

/**
 * 텍스트 템플릿을 코드와 데이터로 분리하여 재구성한다.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class CompiledTemplate {
	private OperationCode code;
	private OperationData data;

	/**
	 * @param code
	 *            OperationCode 템플릿의 코드영역
	 * @param data
	 *            OperationData 템플릿의 데이터영역
	 */
	public CompiledTemplate(OperationCode code, OperationData data) {
		this.code = code;
		this.data = data;
	}

	public String toString() {
		StringBuffer buff = new StringBuffer();

		buff.append("{code=\n");
		buff.append(code);
		buff.append(",data=\n");
		buff.append(data);
		buff.append("}");

		return buff.toString();
	}

	/**
	 * @return OperationCode 템플릿 runtime code 부분
	 */
	public OperationCode getOperationCode() {
		return this.code;
	}

	/**
	 * @return OperationData 템플릿 runtime data 부분
	 */
	public OperationData getOperationData() {
		return this.data;
	}
}
