document.addEventListener("click", (e) => {
if (e.target.matches("[data-toggle-nav]"))
  document.getElementById("mainNav").classList.toggle("open");
});


document.querySelectorAll("form[data-confirm]").forEach((form) =>
form.addEventListener("submit", (e) => {
  if (!confirm(form.dataset.confirm)) e.preventDefault();
}),
);


document.querySelectorAll("form[data-validate]").forEach((form) =>
form.addEventListener("submit", (e) => {
  if (!form.checkValidity()) {
    e.preventDefault();
    form.reportValidity();
  }
}),
);


const canvas = document.getElementById("revenueChart");

if (canvas && window.Chart) {
  let raw = canvas.dataset.points || "[]";
  const labels = [...raw.matchAll(/label=([^,}]+)/g)].map((m) => m[1]);
  const values = [...raw.matchAll(/value=([0-9.]+)/g)].map((m) => Number(m[1]));

  new Chart(canvas, {
    type: "bar",
    data: { labels, datasets: [{ label: "Revenue", data: values }] },
    options: { responsive: true, plugins: { legend: { display: false } } },
  });
}


function handleFileUpload(input) {
  const file = input.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append("file", file);

  const csrfToken = document.querySelector('input[name="_csrf"]')?.value;

  fetch("/manager/books/upload-image", {
    method: "POST",
    body: formData,
    headers: csrfToken ? { "X-CSRF-TOKEN": csrfToken } : {}
  })
  .then(async (res) => {
  const text = await res.text();

  if (!res.ok) {
    throw new Error(text || "Upload failed with status " + res.status);
  }

  return text;
})
  .then(url => {
    const imageUrlInput = document.querySelector('input[name="imageUrl"]');
    const preview = document.getElementById("bookImagePreview");

    if (imageUrlInput) {
      imageUrlInput.value = url;
    }

    if (preview) {
      preview.src = url;
      preview.style.display = "block";
    }
  })
.catch(err => {
  console.error(err);
  alert(err.message);
});
}