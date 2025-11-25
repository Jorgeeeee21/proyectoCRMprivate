import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
// RUTAS CORREGIDAS
import { ClienteService } from '../../core/services/cliente.service'; 
import { Cliente } from '../../core/models/cliente.model';

// Componentes compartidos
import { TableComponent } from '../../shared/components/table/table.component'; 
import { LoadingSpinnerComponent } from '../../shared/components/loading-spinner/loading-spinner.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [
    CommonModule, 
    TableComponent, // Usaremos el componente compartido
    LoadingSpinnerComponent 
  ],
  template: `
    <div class="p-4">
      <div class="flex justify-between items-center mb-6">
        <h1 class="text-3xl font-extrabold text-gray-900">👥 Gestión de Clientes</h1>
        <button 
          (click)="navegarAFormulario()"
          class="px-4 py-2 bg-indigo-600 text-white font-semibold rounded-lg shadow-md hover:bg-indigo-700 transition duration-300"
        >
          + Añadir Nuevo Cliente
        </button>
      </div>

      <!-- Barra de Filtros (Placeholder) -->
      <div class="bg-white p-4 rounded-xl shadow-sm mb-6 border border-gray-200">
        <input 
          type="text" 
          placeholder="Buscar por Nombre o NIF..."
          class="w-full p-2 border border-gray-300 rounded-lg focus:ring-indigo-500 focus:border-indigo-500"
          (input)="filtrarClientes($event)"
        >
      </div>

      <!-- USANDO CONTROL FLOW NATIVO DE ANGULAR (@if, @for) -->
      
      <!-- 1. Indicador de Carga -->
      @if (loading()) {
        <app-loading-spinner class="mx-auto mt-10"></app-loading-spinner>
      } 
      
      <!-- 2. Lista de Clientes (Solo se muestra cuando no está cargando y hay datos) -->
      @if (!loading() && clientesFiltrados().length > 0) {
        <section class="mt-8">
          <app-table [data]="clientesFiltrados()" (edit)="editarCliente($event)" (delete)="eliminarCliente($event)"></app-table>
        </section>
      }
      
      <!-- 3. Estado Vacío -->
      @if (!loading() && clientesFiltrados().length === 0) {
        <div class="text-center p-10 bg-white rounded-xl shadow-lg mt-8">
          <p class="text-xl font-semibold text-gray-500">No se encontraron clientes.</p>
          <p class="text-gray-400 mt-2">Añade tu primer cliente comercial para empezar.</p>
        </div>
      }
    </div>
  `,
  styles: []
})
export class ClienteListComponent implements OnInit {
  // Inyección de dependencias usando inject()
  private clienteService = inject(ClienteService);
  private router = inject(Router);

  // Signals para manejar el estado reactivo
  clientes = signal<Cliente[]>([]);
  loading = signal(false);
  filtroTexto = signal('');

  // Computed: Lista que se recalcula automáticamente al cambiar 'clientes' o 'filtroTexto'
  clientesFiltrados = computed(() => {
    const texto = this.filtroTexto().toLowerCase();
    const lista = this.clientes();
    
    if (!texto) {
      return lista;
    }

    return lista.filter(c =>
      c.nombre.toLowerCase().includes(texto) ||
      (c.cif && c.cif.toLowerCase().includes(texto))
    );
  });

  ngOnInit(): void {
    // Cuando el componente inicia, cargamos los clientes
    this.loadClientes();
  }

  loadClientes(): void {
    this.loading.set(true);
    
    // Consumimos el Observable del servicio
    this.clienteService.getClientes().subscribe({
      next: (data) => {
        // Simulamos los datos
        const simulatedData: Cliente[] = [
          { id: 1, nombre: 'Innovatech Solutions S.L.', cif: 'B12345678', activo: true },
          { id: 2, nombre: 'Global Logistics Corp', cif: 'A98765432', activo: true },
          { id: 3, nombre: 'Cafés La Montaña S.A.', cif: 'Z11223344', activo: false },
        ];
        
        this.clientes.set(simulatedData);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar clientes:', err);
        this.loading.set(false);
      }
    });
  }
  
  // --- Métodos de Interacción con el usuario ---

  navegarAFormulario(): void {
    this.router.navigate(['/clientes/nuevo']);
  }
  
  filtrarClientes(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.filtroTexto.set(input.value);
  }

  editarCliente(cliente: Cliente): void {
    console.log('Editando cliente:', cliente.id);
    this.router.navigate(['/clientes/editar', cliente.id]);
  }

  eliminarCliente(cliente: Cliente): void {
    // Usar un modal custom en lugar de confirm(), pero por simplicidad de ejemplo se mantiene.
    if (confirm(`¿Estás seguro de que quieres eliminar a ${cliente.nombre}?`)) {
      this.clienteService.deleteCliente(cliente.id!).subscribe({
        next: () => {
          console.log('Cliente eliminado con éxito');
          this.loadClientes();
        },
        error: (err) => {
          console.error('Error al eliminar:', err);
        }
      });
    }
  }
}