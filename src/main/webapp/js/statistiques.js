document.addEventListener('DOMContentLoaded', function () {

    const chartData = document.getElementById('chart-data');

    // Read data from hidden element
    const revenueLabels = chartData.getAttribute('data-revenue-labels');
    const revenueValues = chartData.getAttribute('data-revenue-values');
    const rdvLabels = chartData.getAttribute('data-rdv-labels');
    const rdvValues = chartData.getAttribute('data-rdv-values');

    // Revenue chart
    new Chart(document.getElementById('revenueChart'), {
        type: 'bar',
        data: {
            labels: revenueLabels ? revenueLabels.split(',').map(l => l.replace(/'/g, '')) : [],
            datasets: [{
                label: 'Revenus (DH)',
                data: revenueValues ? revenueValues.split(',').map(Number) : [],
                backgroundColor: '#27ae60',
                borderRadius: 6
            }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } }
        }
    });

    // RDV chart
    new Chart(document.getElementById('rdvChart'), {
        type: 'line',
        data: {
            labels: rdvLabels ? rdvLabels.split(',').map(l => l.replace(/'/g, '')) : [],
            datasets: [{
                label: 'Nombre de RDV',
                data: rdvValues ? rdvValues.split(',').map(Number) : [],
                borderColor: '#3498db',
                backgroundColor: 'rgba(52,152,219,0.1)',
                fill: true,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } }
        }
    });

});