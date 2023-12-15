import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface DialogData {
  username: string;
}

@Component({
  selector: 'app-dialog-username-google',
  templateUrl: './dialog-username-google.component.html',
  styleUrls: ['./dialog-username-google.component.css']
})
export class DialogUsernameGoogleComponent  {

  constructor(
    public dialogRef: MatDialogRef<DialogUsernameGoogleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}
