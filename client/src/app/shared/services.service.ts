import { Injectable } from '@angular/core';

@Injectable()
export class ServicesService {

  private customerDetails: any;

  constructor() { }


  setCustomerDetailsForSale(obj: any): void {
      this.customerDetails = obj;
      localStorage.setItem('customerDetails', JSON.stringify(obj));
    }
  
    getCustomerDetailsForSale(): any {
      this.customerDetails = JSON.parse(localStorage.getItem('customerDetails'));
      return this.customerDetails;
    }
    
    clearCustomer(): void {
      localStorage.removeItem('customerDetails');
    }

}
