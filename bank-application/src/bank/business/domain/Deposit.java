package bank.business.domain;

/**
 * @author Ingrid Nunes
 * 
 */
public class Deposit extends Transaction {

	private long envelope;
	private DepositStatus status;
	
	public Deposit(OperationLocation location, CurrentAccount account,
			long envelope, double amount) {
		super(location, account, amount);
		this.envelope = envelope;
		this.status = this.getInitialDepositStatus();
	}

	private DepositStatus getInitialDepositStatus() {
		if(this.getLocation() instanceof ATM) {
			return DepositStatus.PENDING;
		}else if(this.getLocation() instanceof Branch) {
			return DepositStatus.FINISHED;
		} else {
			throw new RuntimeException("Tipo de local desconhecido");
		}
	}
	
	/**
	 * @return the envelope
	 */
	public long getEnvelope() {
		return envelope;
	}

	public DepositStatus getStatus() {
		return status;
	}

	public void setStatus(DepositStatus status) {
		this.status = status;
	}

}
