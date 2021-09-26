package bank.ui.text.command;

import java.util.List;

import bank.business.AccountOperationService;
import bank.business.BusinessException;
import bank.business.domain.Deposit;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

public class ConfirmDepositCommand extends Command{

	private final AccountOperationService accountOperationService;

	public ConfirmDepositCommand(BankTextInterface bankInterface,
			AccountOperationService accountOperationService) {
		super(bankInterface);
		this.accountOperationService = accountOperationService;
	}

	@Override
	public void execute() throws Exception {
		List<Deposit> deposits = accountOperationService.getPendingDeposits();
		printDepositMenu(deposits);
		int option = UIUtils.INSTANCE.readInteger("message.choose.deposit") - 1;
		Deposit chosenDeposit = null;
		if(option >= 0 && option < deposits.size()) {
			chosenDeposit = deposits.get(option);
			changeDepositStatus(chosenDeposit);
		}else {
			throw new RuntimeException("Depósito de número "+option+" não existe");
		}
		System.out.println(getTextManager().getText(
				"message.operation.succesfull"));
	}
	private void printDepositMenu(List<Deposit> deposits) {
		StringBuffer sb = new StringBuffer();
		sb.append(getTextManager().getText("deposit.id")).append("\t");
		sb.append(getTextManager().getText("account.number")).append("\t\t");
		sb.append(getTextManager().getText("date")).append("\t\t\t");
		sb.append(getTextManager().getText("location")).append("\t");
		sb.append(getTextManager().getText("details")).append("\t");
		sb.append(getTextManager().getText("amount")).append("\n");
		sb.append("-----------------------------------------------------------------------------------------------------------\n");
		int i = 1;
		for (Deposit deposit : deposits) {
			sb.append(i++).append("\t\t\t");	
			sb.append(deposit.getAccount().getId().getNumber()).append("\t\t\t");
			sb.append(UIUtils.INSTANCE.formatDateTime(deposit.getDate()))
				.append("\t");	
			sb.append(deposit.getLocation()).append("\t\t");
			sb.append(deposit.getEnvelope()).append("\t\t");			
			sb.append("+ ").append(deposit.getAmount());			 
			sb.append("\n");
		}
		System.out.println(sb);
	}
	private void changeDepositStatus(Deposit deposit) throws BusinessException {
		final int CONFIRM = 1;
		final int CANCEL = 2;
		System.out.println("1 - Confirmar\n2 - Cancelar");
		int option = UIUtils.INSTANCE.readInteger("message.choose.option");
		switch (option) {
		case CONFIRM:
			accountOperationService.confirmDeposit(deposit);
			break;
		case CANCEL:
			accountOperationService.cancelDeposit(deposit);
			break;
		default:
			throw new IllegalArgumentException("Valor inexperado: " + option);
		}
	}
}
