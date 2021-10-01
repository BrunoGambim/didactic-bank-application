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
		printDepositList(deposits);
		int option = UIUtils.INSTANCE.readInteger("message.choose.deposit",0,deposits.size());
		if(option != 0) {
			Deposit chosenDeposit = deposits.get(option - 1);
			finishDeposit(chosenDeposit);
			System.out.println("Status final do depósito: " + getTextManager()
						.getText(chosenDeposit.getStatus().toString()));
			System.out.println(getTextManager().getText(
					"message.operation.succesfull"));
		}
	}
	private void printDepositList(List<Deposit> deposits) {
		System.out.print(getTextManager().getText("deposit.id")+"\t");
		printDepositHeader();
		for (int i = 0; i < deposits.size();i++) {
			System.out.print((i+1)+"\t\t\t");
			printDeposit(deposits.get(i));
		}
	}
	private void printDepositHeader(){
		StringBuffer sb = new StringBuffer();
		sb.append(getTextManager().getText("account.number")).append("\t\t");
		sb.append(getTextManager().getText("date")).append("\t\t\t");
		sb.append(getTextManager().getText("location")).append("\t");
		sb.append(getTextManager().getText("details")).append("\t");
		sb.append(getTextManager().getText("amount")).append("\n");
		sb.append("-----------------------------------------------------------------------------------------------------------");
		System.out.println(sb);
	}
	private void printDeposit(Deposit deposit){
		StringBuffer sb = new StringBuffer();	
		sb.append(deposit.getAccount().getId().getNumber()).append("\t\t\t");
		sb.append(UIUtils.INSTANCE.formatDateTime(deposit.getDate()))
			.append("\t");	
		sb.append(deposit.getLocation()).append("\t\t");
		sb.append(deposit.getEnvelope()).append("\t\t");			
		sb.append("+ ").append(deposit.getAmount());			 
		System.out.println(sb);
	}
	private void finishDeposit(Deposit deposit) throws BusinessException {
		final int CONFIRM = 1;
		final int CANCEL = 2;
		printDepositHeader();
		printDeposit(deposit);
		printOptionsMenu();
		int option = UIUtils.INSTANCE.readInteger("message.choose.option",1,2);
		switch (option) {
		case CONFIRM:
			accountOperationService.confirmDeposit(deposit);
			break;
		case CANCEL:
			accountOperationService.cancelDeposit(deposit);
			break;
		default:
			throw new IllegalArgumentException("Valor inesperado: " + option);
		}
	}
	private void printOptionsMenu() {
		StringBuffer sb = new StringBuffer();	
		sb.append(getTextManager().getText("message.choose.option")).append(":").append("\n");
		sb.append("1-").append(getTextManager().getText("message.confirm")).append("\n");
		sb.append("2-").append(getTextManager().getText("message.cancel")).append("\n");
		System.out.println(sb);
	}
}
