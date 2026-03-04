import {
  Component,
  Output,
  EventEmitter,
  signal,
  effect,
  input,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Client } from '../../models/client.model';
import { ClientFormData } from '../../models/client.dto';

@Component({
  selector: 'app-client-form',
  imports: [FormsModule],
  templateUrl: './client-form.component.html',
  styleUrl: './client-form.component.scss',
})
export class ClientFormComponent {
  // Reactive inputs using input() signals
  isVisible = input<boolean>(false);
  isEditing = input<boolean>(false);
  isSubmitting = input<boolean>(false);
  clientToEdit = input<Client | null>(null);

  @Output() submitForm = new EventEmitter<ClientFormData>();
  @Output() cancel = new EventEmitter<void>();

  formData = signal<ClientFormData>({
    name: '',
    gender: 'M',
    age: 0,
    identification: '',
    address: '',
    phone: '',
    password: '',
    status: true,
  });

  constructor() {
    // Effect to populate form when editing
    effect(() => {
      const client = this.clientToEdit();
      if (client && this.isEditing()) {
        this.formData.set({
          name: client.name,
          gender: client.gender,
          age: client.age,
          identification: client.identification,
          address: client.address,
          phone: client.phone,
          password: client.password,
          status: client.status,
        });
      }
    });
  }

  updateField(field: keyof ClientFormData, value: any): void {
    this.formData.update((data) => ({ ...data, [field]: value }));
  }

  onSubmit(): void {
    const data = this.formData();

    // Basic validation
    if (!data.name || !data.identification || !data.password) {
      return;
    }

    this.submitForm.emit(data);
  }

  onCancel(): void {
    this.resetForm();
    this.cancel.emit();
  }

  private resetForm(): void {
    this.formData.set({
      name: '',
      gender: 'M',
      age: 0,
      identification: '',
      address: '',
      phone: '',
      password: '',
      status: true,
    });
  }

  isFieldValid(field: keyof ClientFormData): boolean {
    const data = this.formData();
    if (
      field === 'name' ||
      field === 'identification' ||
      field === 'password'
    ) {
      return !!data[field];
    }
    return true;
  }
}
