import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogUsernameGoogleComponent } from './dialog-username-google.component';

describe('DialogUsernameGoogleComponent', () => {
  let component: DialogUsernameGoogleComponent;
  let fixture: ComponentFixture<DialogUsernameGoogleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DialogUsernameGoogleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogUsernameGoogleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
