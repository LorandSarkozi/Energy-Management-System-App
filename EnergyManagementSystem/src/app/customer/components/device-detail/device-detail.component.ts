import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../service/customer.service';
import Chart from 'chart.js/auto';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-device-detail',
  templateUrl: './device-detail.component.html',
  styleUrls: ['./device-detail.component.scss'],
})
export class DeviceDetailComponent implements OnInit {
  deviceId: string = ''; // Device ID from the backend
  selectedDate: string = ''; // Selected date for filtering
  consumptionData: any[] = []; // Original data from the backend
  chart: Chart | undefined; // Chart.js instance

  constructor(private customerService: CustomerService,
              private route: ActivatedRoute )
               {}

  ngOnInit(): void {
    this.deviceId = this.route.snapshot.paramMap.get('deviceId') || '';
    this.selectedDate = new Date().toISOString().split('T')[0]; // Default to today's date
    this.loadConsumptionData();
  }

  loadConsumptionData(): void {
    if (this.selectedDate) {
      this.customerService
        .getEnergyConsumptionByDeviceAndDate(this.deviceId, this.selectedDate)
        .subscribe(
          (data) => {
            this.processData(data); // Process and filter data
            this.renderChart(); // Render the chart
          },
          (error) => {
            console.error('Error fetching energy consumption:', error);
          }
        );
    }
  }

  processData(data: any[]): void {
    // Filter out negative hourlyConsumption values and map the timestamp to time (including seconds)
    this.consumptionData = data
      .filter((entry) => entry.hourlyConsumption >= 0) // Exclude negative values
      .map((entry) => ({
        time: new Date(entry.timestamp).toLocaleTimeString([], {
          hour: '2-digit',
          minute: '2-digit',
          second: '2-digit',
        }), // Show time with hour, minute, and second
        value: entry.hourlyConsumption, // Retain the valid hourlyConsumption
      }));
  
    // Assume all data has the same deviceId
    if (data.length > 0) {
      this.deviceId = data[0].deviceId; // Extract the deviceId
    }
  }

  renderChart(): void {
    // Destroy the existing chart instance to avoid duplicates
    if (this.chart) {
      this.chart.destroy();
    }
  
    // Prepare data for the chart
    const labels = this.consumptionData.map((entry) => entry.time); // Use time (hour:minute:second)
    const values = this.consumptionData.map((entry) => entry.value);
  
    // Create a new chart instance
    const ctx = document.getElementById('consumptionChart') as HTMLCanvasElement;
    this.chart = new Chart(ctx, {
      type: 'line', // Use a line chart instead of bar chart to better represent time series
      data: {
        labels: labels, // Labels (time)
        datasets: [
          {
            label: 'Hourly Energy Consumption (kWh)',
            data: values,
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
            tension: 0.1, // Smooth the line between points
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: true,
            position: 'top',
          },
        },
        scales: {
          x: {
            title: {
              display: true,
              text: 'Time of Day (HH:MM:SS)', // Label for time axis
            },
            ticks: {
              maxRotation: 45, // Rotate labels to avoid overlapping
              minRotation: 30,
            },
          },
          y: {
            title: {
              display: true,
              text: 'Energy Consumption (kWh)',
            },
            beginAtZero: true,
          },
        },
      },
    });
  }
  
}
