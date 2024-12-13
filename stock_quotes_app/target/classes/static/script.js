document.addEventListener("DOMContentLoaded", () => {
    const fetchQuoteBtn = document.getElementById("fetchQuoteBtn");

    // Add event listener on the button
    fetchQuoteBtn.addEventListener("click", () => {
        const stockSymbol = document.getElementById("stockSymbol").value.trim();
        if (stockSymbol) {
            fetchStockQuote(stockSymbol);
        } else {
            alert("Please enter a stock symbol.");
        }
    });
});

function fetchStockQuote(symbol) {
    const stockQuoteDiv = document.getElementById("stock-quote");

    // Clear the previous result
    stockQuoteDiv.innerHTML = "";

    fetch(`/api/stock/quote/${symbol}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            displayStockQuote(data);
        })
        .catch(error => {
            stockQuoteDiv.innerHTML = `<p>Error fetching stock quote: ${error.message}</p>`;
        });
}

function displayStockQuote(data) {
    const stockQuoteDiv = document.getElementById("stock-quote");
    stockQuoteDiv.innerHTML = `
        <div class="stock-quote">
            <div class="stock-item"><span class="stock-label">Symbol:</span> ${data.symbol}</div>
            <div class="stock-item"><span class="stock-label">Price:</span> $${data.price.toFixed(2)}</div>
            <div class="stock-item"><span class="stock-label">Change:</span> $${data.change.toFixed(2)}</div>
            <div class="stock-item"><span class="stock-label">Percentage Change:</span> ${data.percentageChange.toFixed(2)}%</div>
            <div class="stock-item"><span class="stock-label">Timestamp:</span> ${new Date(data.timestamp).toLocaleString()}</div>
            <div class="stock-item"><span class="stock-label">Open:</span> $${data.open.toFixed(2)}</div>
            <div class="stock-item"><span class="stock-label">High:</span> $${data.high.toFixed(2)}</div>
            <div class="stock-item"><span class="stock-label">Low:</span> $${data.low.toFixed(2)}</div>
            <div class="stock-item"><span class="stock-label">Close:</span> $${data.close.toFixed(2)}</div>
            <div class="stock-item"><span class="stock-label">Volume:</span> ${data.volume} shares</div>
        </div>
    `;
}
