package net.ion.framework.template;

/**
 * �ؽ�Ʈ ���ø��� �ڵ�� �����ͷ� �и��Ͽ� �籸���Ѵ�.
 * 
 * @author Kim Sanghoon wizest@i-on.net
 * @version 1.0
 */

public class CompiledTemplate {
	private OperationCode code;
	private OperationData data;

	/**
	 * @param code
	 *            OperationCode ���ø��� �ڵ念��
	 * @param data
	 *            OperationData ���ø��� �����Ϳ���
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
	 * @return OperationCode ���ø� runtime code �κ�
	 */
	public OperationCode getOperationCode() {
		return this.code;
	}

	/**
	 * @return OperationData ���ø� runtime data �κ�
	 */
	public OperationData getOperationData() {
		return this.data;
	}
}
