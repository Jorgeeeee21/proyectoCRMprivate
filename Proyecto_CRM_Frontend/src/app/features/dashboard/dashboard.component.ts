import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EstadisticasService, Estadisticas } from '../../core/services/estadisticas.service';
import { LoadingSpinnerComponent } from '../../shared/components/loading-spinner/loading-spinner.component';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, LoadingSpinnerComponent, BaseChartDirective],
  template: `
    <div class="dashboard-container p-6 bg-gray-50 min-h-screen">
      <div class="max-w-7xl mx-auto">
        <!-- Header -->
        <div class="mb-8">
          <h1 class="text-4xl font-bold text-gray-900 mb-2">📊 Dashboard CRM</h1>
          <p class="text-gray-600">Vista general de tu gestión comercial</p>
        </div>

        <!-- Loading State -->
        @if (loading()) {
          <div class="flex justify-center items-center h-64">
            <app-loading-spinner></app-loading-spinner>
          </div>
        }

        <!-- Content -->
        @if (!loading() && estadisticas()) {
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <!-- Card: Clientes Activos -->
            <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-blue-500">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-gray-600 text-sm font-medium">Clientes Activos</p>
                  <p class="text-3xl font-bold text-gray-900 mt-2">{{ estadisticas()!.clientesActivos }}</p>
                  <p class="text-xs text-gray-500 mt-1">de {{ estadisticas()!.totalClientes }} totales</p>
                </div>
                <div class="text-4xl">👥</div>
              </div>
            </div>

            <!-- Card: Total Incidencias -->
            <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-red-500">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-gray-600 text-sm font-medium">Total Incidencias</p>
                  <p class="text-3xl font-bold text-gray-900 mt-2">{{ estadisticas()!.totalIncidencias }}</p>
                  <p class="text-xs text-gray-500 mt-1">Gestionadas</p>
                </div>
                <div class="text-4xl">⚠️</div>
              </div>
            </div>

            <!-- Card: Total Tareas -->
            <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-yellow-500">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-gray-600 text-sm font-medium">Total Tareas</p>
                  <p class="text-3xl font-bold text-gray-900 mt-2">{{ estadisticas()!.totalTareas }}</p>
                  <p class="text-xs text-gray-500 mt-1">{{ estadisticas()!.tareasCompletadas }} completadas</p>
                </div>
                <div class="text-4xl">✅</div>
              </div>
            </div>

            <!-- Card: Tareas Completadas -->
            <div class="bg-white rounded-xl shadow-lg p-6 border-l-4 border-green-500">
              <div class="flex items-center justify-between">
                <div>
                  <p class="text-gray-600 text-sm font-medium">Tasa de Completación</p>
                  <p class="text-3xl font-bold text-gray-900 mt-2">
                    {{ getTasaCompletacion() }}%
                  </p>
                  <p class="text-xs text-gray-500 mt-1">Tareas finalizadas</p>
                </div>
                <div class="text-4xl">📈</div>
              </div>
            </div>
          </div>

          <!-- Charts Section -->
          <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
            <!-- Chart: Incidencias por Estado -->
            <div class="bg-white rounded-xl shadow-lg p-6">
              <h2 class="text-xl font-bold text-gray-900 mb-4">Incidencias por Estado</h2>
              <div class="h-64">
                <canvas baseChart
                  [data]="incidenciasChartData"
                  [type]="incidenciasChartType"
                  [options]="chartOptions">
                </canvas>
              </div>
            </div>

            <!-- Chart: Tareas por Estado -->
            <div class="bg-white rounded-xl shadow-lg p-6">
              <h2 class="text-xl font-bold text-gray-900 mb-4">Tareas por Estado</h2>
              <div class="h-64">
                <canvas baseChart
                  [data]="tareasChartData"
                  [type]="tareasChartType"
                  [options]="chartOptions">
                </canvas>
              </div>
            </div>
          </div>

          <!-- Detailed Stats -->
          <div class="bg-white rounded-xl shadow-lg p-6">
            <h2 class="text-xl font-bold text-gray-900 mb-4">Detalle de Estados</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- Incidencias -->
              <div>
                <h3 class="text-lg font-semibold text-gray-700 mb-3">Incidencias</h3>
                <div class="space-y-2">
                  @for (item of getIncidenciasArray(); track item.estado) {
                    <div class="flex justify-between items-center p-2 bg-gray-50 rounded">
                      <span class="text-sm font-medium text-gray-700">{{ item.estado }}</span>
                      <span class="text-sm font-bold text-gray-900">{{ item.cantidad }}</span>
                    </div>
                  }
                </div>
              </div>

              <!-- Tareas -->
              <div>
                <h3 class="text-lg font-semibold text-gray-700 mb-3">Tareas</h3>
                <div class="space-y-2">
                  @for (item of getTareasArray(); track item.estado) {
                    <div class="flex justify-between items-center p-2 bg-gray-50 rounded">
                      <span class="text-sm font-medium text-gray-700">{{ item.estado }}</span>
                      <span class="text-sm font-bold text-gray-900">{{ item.cantidad }}</span>
                    </div>
                  }
                </div>
              </div>
            </div>
          </div>
        }

        <!-- Error State -->
        @if (!loading() && !estadisticas() && error()) {
          <div class="bg-red-50 border border-red-200 rounded-xl p-6 text-center">
            <p class="text-red-800 font-semibold">Error al cargar estadísticas</p>
            <p class="text-red-600 text-sm mt-2">{{ error() }}</p>
            <button 
              (click)="loadEstadisticas()"
              class="mt-4 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition">
              Reintentar
            </button>
          </div>
        }
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container {
      font-family: 'Inter', sans-serif;
    }
  `]
})
export class DashboardComponent implements OnInit {
  private estadisticasService = inject(EstadisticasService);

  estadisticas = signal<Estadisticas | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);

  // Chart configurations
  incidenciasChartType: ChartType = 'doughnut';
  tareasChartType: ChartType = 'doughnut';
  incidenciasChartData: ChartData<'doughnut'> = { labels: [], datasets: [] };
  tareasChartData: ChartData<'doughnut'> = { labels: [], datasets: [] };

  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
      },
    },
  };

  ngOnInit(): void {
    this.loadEstadisticas();
  }

  loadEstadisticas(): void {
    this.loading.set(true);
    this.error.set(null);

    this.estadisticasService.getEstadisticas().subscribe({
      next: (data) => {
        this.estadisticas.set(data);
        this.updateCharts(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar estadísticas:', err);
        this.error.set('No se pudo conectar con el servidor. Asegúrate de que el backend esté ejecutándose.');
        this.loading.set(false);
      }
    });
  }

  updateCharts(data: Estadisticas): void {
    // Incidencias Chart
    const incidenciasLabels = Object.keys(data.incidenciasPorEstado);
    const incidenciasValues = Object.values(data.incidenciasPorEstado);
    
    this.incidenciasChartData = {
      labels: incidenciasLabels,
      datasets: [{
        data: incidenciasValues,
        backgroundColor: [
          '#EF4444', // Rojo - ABIERTA
          '#F59E0B', // Amarillo - EN_PROCESO
          '#10B981', // Verde - RESUELTA
          '#6B7280', // Gris - CERRADA
        ],
      }]
    };

    // Tareas Chart
    const tareasLabels = Object.keys(data.tareasPorEstado);
    const tareasValues = Object.values(data.tareasPorEstado);
    
    this.tareasChartData = {
      labels: tareasLabels,
      datasets: [{
        data: tareasValues,
        backgroundColor: [
          '#3B82F6', // Azul - PENDIENTE
          '#F59E0B', // Amarillo - EN_PROGRESO
          '#10B981', // Verde - COMPLETADA
          '#EF4444', // Rojo - CANCELADA
        ],
      }]
    };
  }

  getTasaCompletacion(): number {
    const stats = this.estadisticas();
    if (!stats || stats.totalTareas === 0) return 0;
    return Math.round((stats.tareasCompletadas / stats.totalTareas) * 100);
  }

  getIncidenciasArray(): Array<{estado: string, cantidad: number}> {
    const stats = this.estadisticas();
    if (!stats) return [];
    return Object.entries(stats.incidenciasPorEstado).map(([estado, cantidad]) => ({
      estado: this.formatEstado(estado),
      cantidad: cantidad as number
    }));
  }

  getTareasArray(): Array<{estado: string, cantidad: number}> {
    const stats = this.estadisticas();
    if (!stats) return [];
    return Object.entries(stats.tareasPorEstado).map(([estado, cantidad]) => ({
      estado: this.formatEstado(estado),
      cantidad: cantidad as number
    }));
  }

  formatEstado(estado: string): string {
    return estado.replace(/_/g, ' ').toLowerCase()
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }
}
