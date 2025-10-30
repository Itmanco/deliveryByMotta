function updateMap(event, linkElement) {
    event.preventDefault(); 
    const address = linkElement.getAttribute('data-address');
    const shopName = linkElement.getAttribute('data-shop-name');
    
    const mapFrame = document.getElementById('google-map-frame');
    const mapStatus = document.getElementById('map-status');

    if (address) {
        const fullAddress = address + ', 札幌, 日本';
        const encodedAddress = encodeURIComponent(fullAddress);
        const mapEmbedUrl = 
            `https://maps.google.com/maps?q=${encodedAddress}&output=embed&z=15`; 
        mapFrame.src = mapEmbedUrl;
        mapStatus.textContent = `${shopName}\n(${address}) の地図を表示中。`;
    } else {
        mapStatus.textContent = "住所データが見つかりません。";
    }
}

// Ensure window.onload is also updated to use the new HTML structure
window.onload = function() {
    const firstLink = document.querySelector('.shop-table-area table tbody tr a');
    if (firstLink) {
        firstLink.click(); 
    }
}