import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CheckoutComponent } from './checkout.component';
import { SharedModule } from '../shared/shared.module';
import { ShippingComponent } from './shipping/shipping.component';
import { PaymentComponent } from './payment/payment.component';
import { AddressComponent } from './address/address.component';

@NgModule({
  imports: [
    CommonModule,
    SharedModule
  ],
  declarations: [CheckoutComponent, ShippingComponent, PaymentComponent, AddressComponent]
})
export class CheckoutModule { }
