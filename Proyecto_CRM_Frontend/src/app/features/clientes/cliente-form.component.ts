import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteService } from '../../core/services/cliente.service';
import { Cliente } from '../../core/models/cliente.model';
import { switchMap } from 'rxjs';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="p-4 max-w-3xl mx-auto bg-white rounded-xl shadow-lg mt-8">
      <h1 class="text-2xl font-bold mb-6 text-gray-800">
        {{ isEditing() ? '✏️ Editar Cliente: ' + clienteId() : '➕ Nuevo Cliente' }}
      </h1>

      <!-- Formulario Reactivo -->
      <form [formGroup]="clienteForm" (ngSubmit)="onSubmit()" class="space-y-4">
        
        <!-- Nombre de la Empresa -->
        <div>
          <label for="nombre" class="block text-sm font-medium text-gray-700">Nombre de la Empresa</label>
          <input id="nombre" type="text" formControlName="nombreEmpresa" 
                 class="mt-1 block w-full border border-gray-300 rounded-md p-2" />
          @if (clienteForm.get('nombreEmpresa')?.invalid && (clienteForm.get('nombreEmpresa')?.dirty || clienteForm.get('nombreEmpresa')?.touched)) {
            <p class="text-red-500 text-xs mt-1">El nombre es obligatorio.</p>
          }
        </div>
        
        <!-- NIF -->
        <div>
          <label for="nif" class="block text-sm font-medium text-gray-700">NIF / CIF</label>
          <input id="nif" type="text" formControlName="nif" 
                 class="mt-1 block w-full border border-gray-300 rounded-md p-2" />
        </div>
        
        <!-- Sector -->
        <div>
          <label for="sector" class="block text-sm font-medium text-gray-700">Sector</label>
          <select id="sector" formControlName="sector" 
                  class="mt-1 block w-full border border-gray-300 rounded-md p-2 bg-white">
            <option value="">Selecciona un sector</option>
            <option value="Tecnología">Tecnología</option>
            <option value="Finanzas">Finanzas</option>
            <option value="Retail">Retail</option>
            <option value="Transporte">Transporte</option>
          </select>
        </div>
        
        <!-- Ingresos Anuales -->
        <div>
          <label for="ingresos" class="block text-sm font-medium text-gray-700">Ingresos Anuales (€)</label>
          <input id="ingresos" type="number" formControlName="ingresosAnuales" 
                 class="mt-1 block w-full border border-gray-300 rounded-md p-2" />
        </div>

        <!-- Botones de Acción -->
        <div class="flex justify-end space-x-4 pt-4">
          <button type="button" (click)="clienteForm.reset()" class="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition">
            Resetear
          </button>
          <button type="submit" [disabled]="clienteForm.invalid" 
                  class="px-4 py-2 text-white font-semibold rounded-lg shadow-md transition duration-150"
                  [ngClass]="{'bg-indigo-600 hover:bg-indigo-700': clienteForm.valid, 'bg-indigo-400 cursor-not-allowed': clienteForm.invalid}">
            {{ isEditing() ? 'Guardar Cambios' : 'Crear Cliente' }}
          </button>
        </div>
      </form>
    </div>
  `,
  styles: []
})
export class ClienteFormComponent implements OnInit {
  
  // Inyección de servicios
  private fb = inject(FormBuilder);
  private clienteService = inject(ClienteService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  // Signals para manejar el estado del formulario
  isEditing = signal(false);
  clienteId = signal<number | null>(null);
  
  // Definición del formulario reactivo
  clienteForm: FormGroup = this.fb.group({
    id: [null], // Se rellena solo en modo edición
    nombreEmpresa: ['', Validators.required],
    nif: ['', [Validators.required, Validators.minLength(9)]],
    sector: ['', Validators.required],
    fechaAlta: [new Date(), Validators.required],
    activo: [true],
    ingresosAnuales: [0, Validators.min(0)],
    contactoPrincipalId: [null]
  });

  ngOnInit(): void {
    // Lógica para detectar si estamos en modo edición o creación
    this.route.paramMap.pipe(
      switchMap(params => {
        const id = params.get('id');
        if (id) {
          this.isEditing.set(true);
          this.clienteId.set(+id);
          // Si estamos editando, obtenemos los datos del cliente del servicio
          return this.clienteService.getClienteById(+id);
        } else {
          this.isEditing.set(false);
          // Si estamos creando, devolvemos un Observable vacío
          return [null];
        }
      })
    ).subscribe(cliente => {
      if (cliente) {
        // Rellenamos el formulario con los datos recibidos
        this.clienteForm.patchValue(cliente);
      }
    });
  }
  
  onSubmit(): void {
    if (this.clienteForm.invalid) {
      console.error('El formulario es inválido.');
      this.clienteForm.markAllAsTouched(); // Marca todos los campos para que se muestren los errores
      return;
    }

    const clienteData: Cliente = this.clienteForm.value;

    const operation = this.isEditing() 
      ? this.clienteService.updateCliente(clienteData)
      : this.clienteService.createCliente(clienteData);

    operation.subscribe({
      next: (res) => {
        console.log(`Cliente ${this.isEditing() ? 'actualizado' : 'creado'} con éxito`, res);
        // Navegar de vuelta a la lista tras guardar
        this.router.navigateByUrl('/clientes');
      },
      error: (err) => {
        console.error('Error al guardar el cliente:', err);
        // Aquí usarías el NotificationModal para un mensaje de error
      }
    });
  }
}