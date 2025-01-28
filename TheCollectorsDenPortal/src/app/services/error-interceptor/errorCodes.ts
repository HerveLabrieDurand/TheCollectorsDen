export const BACKEND_ERROR_CODES = {
  DATABASE_OPERATION: 'DatabaseOperationException',
  EMAIL_NOT_REGISTERED: 'EmailNotRegisteredException',
  EMAIL_ALREADY_IN_USE: 'EmailAlreadyInUseException',
  EMAIL_ALREADY_CONFIRMED: 'EmailAlreadyConfirmedException',
  INVALID_CREDENTIALS: 'InvalidCredentialsException',
  TOKEN_EXPIRED: 'TokenExpiredException',
  TOKEN_INVALID: 'TokenInvalidException',
} as const;
