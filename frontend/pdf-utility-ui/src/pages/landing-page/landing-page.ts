import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Footer } from "../../shared/footer/footer";

@Component({
  selector: 'app-landing-page',
  imports: [Footer],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {

}
