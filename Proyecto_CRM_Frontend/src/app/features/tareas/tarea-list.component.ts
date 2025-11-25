import { Component } from '@angular/core';

@Component({
  selector: 'app-tarea-list',
  standalone: true,
  template: `
    <div class="tarea-list-container">
      <h1>Gestión de Tareas</h1>
      <p>Componente de lista de tareas - Pendiente de implementar</p>
    </div>
  `,
  styles: [`
    .tarea-list-container {
      padding: 2rem;
    }
  `]
})
export class TareaListComponent {
}

