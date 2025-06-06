import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import {
	FormBuilder,
	FormControl,
	ReactiveFormsModule,
	Validators
} from '@angular/forms';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-labseq-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './labseq-form.component.html',
  styleUrls: ['./labseq-form.component.css'],
})
export class LabseqFormComponent {
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);

  form = this.fb.group({
    index: new FormControl<number>(0, {
      nonNullable: true,
      validators: [Validators.required, Validators.min(0)],
    }),
  });

  loading = signal(false);
  result = signal<string | null>(null);

  onSubmit(): void {
    if (this.form.invalid) return;
	

    const index = this.form.value.index!;

	this.loading.set(true);
	this.result.set(null);

    this.http
      .get(`${environment.apiBaseUrl}/labseq/${index}`, {
        responseType: 'text',
      })
      .subscribe({
        next: (res) => this.result.set(res),
        error: (err) => {
		  this.loading.set(false)
          console.error(err);
          this.result.set('An error occurred');
        },
		complete: () => this.loading.set(false),
      });
  }
}
