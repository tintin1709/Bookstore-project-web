document.addEventListener('click', e => { if (e.target.matches('[data-toggle-nav]')) document.getElementById('mainNav').classList.toggle('open'); });
document.querySelectorAll('form[data-confirm]').forEach(form => form.addEventListener('submit', e => { if (!confirm(form.dataset.confirm)) e.preventDefault(); }));
document.querySelectorAll('form[data-validate]').forEach(form => form.addEventListener('submit', e => { if (!form.checkValidity()) { e.preventDefault(); form.reportValidity(); } }));
const canvas = document.getElementById('revenueChart');
if (canvas && window.Chart) {
  let raw = canvas.dataset.points || '[]';
  const labels = [...raw.matchAll(/label=([^,}]+)/g)].map(m => m[1]);
  const values = [...raw.matchAll(/value=([0-9.]+)/g)].map(m => Number(m[1]));
  new Chart(canvas, {type:'bar', data:{labels, datasets:[{label:'Revenue', data:values}]}, options:{responsive:true, plugins:{legend:{display:false}}}});
}
