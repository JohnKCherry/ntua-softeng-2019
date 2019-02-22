package gr.ntua.ece.softeng18b.conf;

public class ConfigurationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5696300689459508920L;

	public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
