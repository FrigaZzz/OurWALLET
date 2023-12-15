import {Component, Inject, OnInit, ViewEncapsulation, NgZone} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import {FormBuilder, FormGroup} from "@angular/forms";
@Component({
  selector: 'app-filters-modal',
  templateUrl: './filters-modal.component.html',
  styleUrls: ['./filters-modal.component.css']
})
export class FiltersModalComponent implements OnInit {
    data;
    description:string;
    invalidAmount=false;

    constructor(
        private fb: FormBuilder,
        private dialogRef: MatDialogRef<FiltersModalComponent>,
        @Inject(MAT_DIALOG_DATA) {minAmount,maxAmount,
          toDate,fromDate} ) {


        this.data = {
            minAmount: minAmount,
            maxAmount: maxAmount,
            toDate: toDate,
            fromDate: fromDate,

        }


    }

    ngOnInit() {

    }

    verify(value,target,save?){

      var regexp =/^[+-]?\d+(\.\d+)?$/

      let check=!save?(value!='-'&&value!=''):save
      
      if(!regexp.test(value)&&check)
      {
        if(target==0)
          this.data.minAmount=null
        else
          this.data.maxAmount=null
        this.invalidAmount=true
        return false
      }
      this.invalidAmount=false

      return true
    }

    save() {
      var regexp =/^[+-]?\d+(\.\d+)?$/
      if(!regexp.test(this.data.minAmount))
        this.data.minAmount=null

      if(!regexp.test(this.data.maxAmount))
        this.data.maxAmount=null
      
    
      this.dialogRef.close(this.data);
}

    close() {
        this.dialogRef.close()
    }

    reset() {
      this.data ={
        minAmount: null,
        maxAmount: null,
        toDate: null,
        fromDate: null,
   };  
  }
}
