### What happens when you type ‘google.com’ in the browser address bar

---

Good morning everyone, Thank you for taking the time to join me for this presentation.
my name is K, and I'm a web developer.

In today’s presentation I would like to talk about What happens when you type ‘google.com’ in the browser address bar.

To begin with, Every website has their own IP address like ‘142.250.206.238’ (google’s ip address).
However, remembering IP addresses can be challenging and they are hard to read or understand.
Here, we can use DNS(Domain Name System) to solve this. DNS stores information about IP in easily readable address such as alphabetic characters.
Lets type 'www.google.com' into your browser's search bar and hit Enter.

Secondly, browser look at cached(stored) DNS records to control internet traffic and make data transfer faster. They first check the browser's own memory, then the computer's memory, then the router's memory, and finally, the memory of the internet service provider (ISP).
If the IP for a URL isn't in the cache, the ISP keeps searching DNS servers until it finds the IP address.

Last but not least, The browser connects to the server using TCP. It sends an HTTP request for the 'www.google.com' webpage. The server processes the request and sends back a response with the webpage. The browser then displays the webpage and cache it to avoid reloading when revisited.

Take a look at the picture below. It shows the steps we discussed in a simple way.

![963074bc-7e7d-45f2-a4c0-6c2dd05dc190](https://github.com/devlkhkr/TIL/assets/84236655/d1c1d1c0-3425-43f6-9b28-21d5d29d3cee)

In conclusion, it's important to understand that a lot happens behind the scenes when we search for "Google". and, we might guess why the response was slow sometimes.

Thank you for your attention! and If you have any questions or comments, I'll be happy to answer them.
