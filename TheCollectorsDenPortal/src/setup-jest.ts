import { setupZoneTestEnv } from 'jest-preset-angular/setup-env/zone';

setupZoneTestEnv();

// Suppress specific CSS parsing errors from PrimeNG (or other dependencies)
const originalConsoleError = console.error;

console.error = (...args) => {
  const errorMessage = args[0];
  if (errorMessage?.type === 'css parsing') {
    return;
  }
  originalConsoleError(...args);
};
