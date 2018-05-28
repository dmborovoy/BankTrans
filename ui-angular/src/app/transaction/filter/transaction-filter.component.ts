import {Component, EventEmitter} from "@angular/core";
import {Subject} from "rxjs/Subject";
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/observable/of';

@Component({
  selector: 'transaction-filter',
  templateUrl: './transaction-filter.component.html',
  styleUrls: ['./transaction-filter.component.css']
})

export class TransactionFilterComponent {
  // private from: Date;
  // private to: Date;
  // private statementId: string;
  // private instance: string;
  // private status: string;
  // private fundsCode: string;
  // private informationToAccountOwner: string;
  // private referenceForAccountOwner: string;
  // private referenceForBank: string;
  // private transactionDescription: string;
  private filterParams: string;


  fieldChange = new EventEmitter();
  keyUp = new Subject();

  constructor(){
    this.keyUp.asObservable()
      .debounceTime(1500)
      .subscribe(data => {
        this.onChanged();
        this.fieldChange.emit();
      });
  }

  onChanged(){
    console.log(
      JSON.stringify(this, function(key, value){
        if (key == "keyUp" || key == "fieldChange" || key == "filterParams") return undefined;
        return value;
  }));

    this.filterParams =
    JSON.stringify(this, function(key, value){
      if (key == "keyUp" || key == "fieldChange" || key == "filterParams") return undefined;
      return value;

    })
  }
}
