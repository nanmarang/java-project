const STORAGE_KEY = "expenseflow-entries-v1";
const budgetLimit = 4200;

const defaultEntries = [
  { id: 1, title: "Groceries", category: "Food", amount: 156.4, date: "2026-09-08" },
  { id: 2, title: "Metro card", category: "Transport", amount: 42.0, date: "2026-09-09" },
  { id: 3, title: "Internet bill", category: "Bills", amount: 68.9, date: "2026-09-12" },
  { id: 4, title: "Cinema night", category: "Entertainment", amount: 24.5, date: "2026-09-15" },
  { id: 5, title: "Pharmacy", category: "Health", amount: 58.0, date: "2026-09-17" },
  { id: 6, title: "Rent", category: "Housing", amount: 1240.0, date: "2026-09-01" }
];

const state = {
  entries: loadEntries(),
  activeCategory: "all",
  query: ""
};

const form = document.getElementById("expenseForm");
const expenseList = document.getElementById("expenseList");
const categoryBreakdown = document.getElementById("categoryBreakdown");
const filterPills = document.getElementById("filterPills");
const searchInput = document.getElementById("searchInput");
const themeToggle = document.getElementById("themeToggle");
const toast = document.getElementById("toast");

const formatCurrency = (value) =>
  new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2
  }).format(value);

function loadEntries() {
  const saved = localStorage.getItem(STORAGE_KEY);
  if (!saved) return [...defaultEntries];

  try {
    const parsed = JSON.parse(saved);
    return Array.isArray(parsed) && parsed.length ? parsed : [...defaultEntries];
  } catch (error) {
    return [...defaultEntries];
  }
}

function saveEntries() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state.entries));
}

function getVisibleEntries() {
  const query = state.query.trim().toLowerCase();

  return [...state.entries]
    .filter((entry) => {
      const matchesCategory = state.activeCategory === "all" || entry.category === state.activeCategory;
      const matchesQuery =
        !query ||
        entry.title.toLowerCase().includes(query) ||
        entry.category.toLowerCase().includes(query);

      return matchesCategory && matchesQuery;
    })
    .sort((a, b) => new Date(b.date) - new Date(a.date));
}

function updateMetric(id, value) {
  const el = document.getElementById(id);
  if (el) el.textContent = value;
}

function renderSummary() {
  const currentMonth = new Date().getMonth();
  const currentYear = new Date().getFullYear();

  const monthlyEntries = state.entries.filter((entry) => {
    const date = new Date(entry.date);
    return date.getMonth() === currentMonth && date.getFullYear() === currentYear;
  });

  const total = monthlyEntries.reduce((sum, entry) => sum + Number(entry.amount), 0);
  const average = monthlyEntries.length ? total / monthlyEntries.length : 0;
  const largest = monthlyEntries.reduce(
    (max, entry) => (Number(entry.amount) > Number(max.amount) ? entry : max),
    { amount: 0 }
  );
  const remaining = budgetLimit - total;

  updateMetric("totalSpend", formatCurrency(total));
  updateMetric("avgSpend", formatCurrency(average));
  updateMetric("largestSpend", formatCurrency(largest.amount || 0));
  updateMetric("remainingSpend", formatCurrency(remaining));

  const progress = Math.min((total / budgetLimit) * 100, 100);
  const budgetProgress = document.getElementById("budgetProgress");
  const budgetStatus = document.getElementById("budgetStatus");
  const budgetValue = document.getElementById("budgetValue");

  budgetValue.textContent = formatCurrency(budgetLimit);
  budgetProgress.style.width = `${progress}%`;
  budgetStatus.textContent = `${formatCurrency(Math.max(remaining, 0))} left to spend`;
}

function renderCategoryBreakdown() {
  const totals = {};
  state.entries.forEach((entry) => {
    totals[entry.category] = (totals[entry.category] || 0) + Number(entry.amount);
  });

  const items = Object.entries(totals).sort((a, b) => b[1] - a[1]);
  const maxAmount = Math.max(...items.map(([, value]) => value), 1);

  categoryBreakdown.innerHTML = items.length
    ? items
        .map(([category, amount]) => {
          const width = (amount / maxAmount) * 100;
          return `
            <div class="category-row">
              <div class="category-meta">
                <span>${category}</span>
                <strong>${formatCurrency(amount)}</strong>
              </div>
              <div class="bar-track">
                <span class="bar-fill" style="width: ${width}%"></span>
              </div>
            </div>
          `;
        })
        .join("")
    : '<div class="empty-state">No spending data yet.</div>';
}

function renderFilters() {
  const categories = ["all", ...new Set(state.entries.map((entry) => entry.category))];

  filterPills.innerHTML = categories
    .map((category) => {
      const isActive = category === state.activeCategory;
      const label = category === "all" ? "All" : category;
      return `
        <button class="filter-button ${isActive ? "active" : ""}" type="button" data-category="${category}">
          ${label}
        </button>
      `;
    })
    .join("");

  filterPills.querySelectorAll(".filter-button").forEach((button) => {
    button.addEventListener("click", () => {
      state.activeCategory = button.dataset.category;
      renderAll();
    });
  });
}

function renderExpenses() {
  const visibleEntries = getVisibleEntries();

  if (!visibleEntries.length) {
    expenseList.innerHTML = '<div class="empty-state">No expenses match your search.</div>';
    return;
  }

  expenseList.innerHTML = visibleEntries
    .map(
      (entry) => `
        <article class="expense-item">
          <div class="expense-main">
            <p class="expense-title">${entry.title}</p>
            <div class="expense-meta">
              <span>${new Date(entry.date).toLocaleDateString("en-US", {
                month: "short",
                day: "numeric",
                year: "numeric"
              })}</span>
              <span>•</span>
              <span>${entry.category}</span>
            </div>
          </div>

          <div class="expense-amount">${formatCurrency(entry.amount)}</div>
          <span class="expense-badge">${entry.category}</span>
          <button class="delete-button" type="button" data-id="${entry.id}">Delete</button>
        </article>
      `
    )
    .join("");

  expenseList.querySelectorAll(".delete-button").forEach((button) => {
    button.addEventListener("click", (event) => {
      const id = Number(event.currentTarget.dataset.id);
      state.entries = state.entries.filter((entry) => entry.id !== id);
      saveEntries();
      renderAll();
      showToast("Expense removed successfully.");
    });
  });
}

function showToast(message) {
  toast.textContent = message;
  toast.classList.add("visible");
  window.clearTimeout(showToast.timer);
  showToast.timer = window.setTimeout(() => toast.classList.remove("visible"), 2200);
}

function renderAll() {
  renderSummary();
  renderCategoryBreakdown();
  renderFilters();
  renderExpenses();
}

function handleSubmit(event) {
  event.preventDefault();

  const formData = new FormData(form);
  const title = String(formData.get("title") || "").trim();
  const category = String(formData.get("category") || "Other");
  const amount = Number(formData.get("amount"));
  const date = String(formData.get("date") || new Date().toISOString().slice(0, 10));

  if (!title || !Number.isFinite(amount) || amount <= 0) {
    showToast("Please provide a valid title and amount.");
    return;
  }

  const nextEntry = {
    id: Date.now(),
    title,
    category,
    amount,
    date
  };

  state.entries = [nextEntry, ...state.entries];
  saveEntries();
  form.reset();
  renderAll();
  showToast("Expense added successfully.");
}

function initTheme() {
  const theme = localStorage.getItem("expenseflow-theme") || "dark";
  document.body.classList.toggle("light-mode", theme === "light");
  themeToggle.textContent = theme === "dark" ? "Light mode" : "Dark mode";
}

function toggleTheme() {
  const isLight = document.body.classList.toggle("light-mode");
  const nextTheme = isLight ? "light" : "dark";
  localStorage.setItem("expenseflow-theme", nextTheme);
  themeToggle.textContent = nextTheme === "dark" ? "Light mode" : "Dark mode";
}

form.addEventListener("submit", handleSubmit);
searchInput.addEventListener("input", (event) => {
  state.query = event.target.value;
  renderExpenses();
});
themeToggle.addEventListener("click", toggleTheme);

initTheme();
renderAll();
