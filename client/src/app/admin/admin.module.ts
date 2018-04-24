import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './admin.component';
import { AdminService } from './admin.service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [AdminComponent],
  providers: [AdminService]

})
export class AdminModule { }
