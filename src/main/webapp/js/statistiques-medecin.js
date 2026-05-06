document.addEventListener('DOMContentLoaded', function () {

    const chartData = document.getElementById('chart-data');

    const consLabels = chartData.getAttribute('data-cons-labels');
    const consValues = chartData.getAttribute('data-cons-values');
    const revenueLabels = chartData.getAttribute('data-revenue-labels');
    const revenueValues = chartData.getAttribute('data-revenue-values');

    // Consultations par mois
    new Chart(document.getElementById('consultationsChart'), {
        type: 'bar',
        data: {
            labels: consLabels ? consLabels.split(',').map(l => l.replace(/'/g, '')) : [],
            datasets: [{
                label: 'Consultations',
                data: consValues ? consValues.split(',').map(Number) : [],
                backgroundColor: '#27ae60',
                borderRadius: 6,
                barThickness: 60
            }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } }
        }
    });

    // Revenus par mois
    new Chart(document.getElementById('revenueChart'), {
        type: 'bar',
        data: {
            labels: revenueLabels ? revenueLabels.split(',').map(l => l.replace(/'/g, '')) : [],
            datasets: [{
                label: 'Revenus (DH)',
                data: revenueValues ? revenueValues.split(',').map(Number) : [],
                borderColor: '#27ae60',
                backgroundColor: 'rgba(39,174,96,0.1)',
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