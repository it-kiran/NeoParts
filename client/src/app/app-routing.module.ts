import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { HeaderComponent } from './shared/header/header.component';
import { HomeComponent } from './home/home.component';
import { ProductPageComponent } from './product-page/product-page.component';
import { CartComponent } from './cart/cart.component';
import { SignupComponent } from './customer/signup/signup.component';

const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'product/:id', component: ProductPageComponent},
  { path: 'viewCart', component: CartComponent},
  { path: 'customer/signup', component: SignupComponent},



  // { path: 'sell/:id/:id', component: SellComponent },
  // { path: '**', redirectTo: '' },
];
@NgModule({

  imports: [
    CommonModule,
    RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
