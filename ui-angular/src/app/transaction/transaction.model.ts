export interface Transaction {
  id: number;
  date: string;
  entryDate: string;
  fundsCode: string;
  amount: string;
  currency: string;
  swiftCode: string;
  referenceForAccountOwner: string;
  referenceForBank: string;
  transactionDescription: string;
  statementID: string;
  transactionStatus: string;
  errorDescription: string;
  instance: string;
  entryOrder: number;
  informationToAccountOwner: string;
  content: Array<Transaction>;

}
