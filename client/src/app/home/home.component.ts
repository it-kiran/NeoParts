import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BackendService } from '../services/backend.service';
import { Product, ProductPageComponent } from '../product-page/product-page.component';
import * as $ from 'jquery';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  featuredProductDto: Product[] = [];
  newProductDto: Product[] = [];
  constructor(private router: Router, private route: ActivatedRoute, private backEndService: BackendService) { }

  ngOnInit() {

    this.getAllFeaturedProducts();
    this.getAllNewProducts();
    // this.loadSlider();
  }

  getAllFeaturedProducts(){
    this.backEndService.getFeaturedProduct()
    .subscribe((prodcut)=>{
      this.featuredProductDto = prodcut;
    });
  }

  getAllNewProducts(){
    this.backEndService.getNewProducts()
    .subscribe((prodcut)=>{
      this.newProductDto = prodcut;
    });
  }

  // loadSlider() {
  //   $('.product-slider').owlCarousel({
  //     loop:true,
  //     margin:10,
  //     nav:true,
  //     responsive:{
  //         0:{
  //             items:1
  //         },
  //         600:{
  //             items:3
  //         },
  //         1000:{
  //             items:5
  //         }
  //     }
  // })
  // }

}
