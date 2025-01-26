import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

// Minimum: 8 char in length, 1 digit, 1 lowercase, 1 uppercase
export const passwordValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const password = control.value;
  const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

  return regex.test(password) ? null : { invalidPassword: true };
};

// # This expression matches three different formats of postal codes:
// 5 digit US ZIP code, 5 digit US ZIP code + 4, and 6 digit alphanumeric Canadian Postal Code.
// The first one must be 5 numeric digits. The ZIP+4 must be 5 numeric digits, a hyphen, and then 4 numeric digits.
// The Canadian postal code must be of the form ANA NAN where A is any uppercase alphabetic character and N is a numeric digit from 0 to 9.
export const postalCodeValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const postalCode = control.value;
  const regex = /^((\d{5}-\d{4})|(\d{5})|([A-Z]\d[A-Z]\d[A-Z]\d))$/;

  return regex.test(postalCode) ? null : { invalidPostalCode: true };
};

//10 digits
export const phoneNumberValidator: ValidatorFn = (
  control: AbstractControl,
): ValidationErrors | null => {
  const phoneNumber = control.value;
  const regex = /^\d{10}$/;

  return regex.test(phoneNumber) ? null : { invalidPhoneNumber: true };
};
