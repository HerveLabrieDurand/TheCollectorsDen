import { AsyncPipe, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSliderModule } from '@angular/material/slider';
import { LoadingService } from '../services/loading/loading.service';

@Component({
  selector: 'app-loading-spinner',
  imports: [AsyncPipe, NgIf, MatSliderModule, MatProgressSpinnerModule],
  templateUrl: './loading-spinner.component.html',
  styleUrl: './loading-spinner.component.css',
})
export class LoadingSpinnerComponent {
  constructor(public loadingService: LoadingService) {}
}
