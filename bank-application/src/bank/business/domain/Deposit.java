package bank.business.domain;

import bank.business.BusinessException;

/**
 * @author Ingrid Nunes
 * 
 */
public class Deposit extends Transaction {

	private long envelope;
	private DepositStatus status;
	
	public Deposit(OperationLocation location, CurrentAccount account,
			long envelope, double amount) throws BusinessException {
		super(location, account, amount);
		this.envelope = envelope;
		setInitialDepositStatus();
	}

	private void setInitialDepositStatus() throws BusinessException {
		if(this.getLocation() instanceof ATM) {
			setStatus(DepositStatus.PENDING);
		}else if(this.getLocation() instanceof Branch) {
			setStatus(DepositStatus.FINISHED);
		} else {
			throw new BusinessException("exception.unknownLocation");
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
