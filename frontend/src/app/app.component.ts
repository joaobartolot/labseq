import { Component } from '@angular/core';
import { LabseqFormComponent } from './components/labseq-form/labseq-form.component';

@Component({
  selector: 'app-root',
  imports: [LabseqFormComponent],
  template: `<app-labseq-form />`
})
export class AppComponent {
  title = 'labseq';
}
