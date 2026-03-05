import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('jwt_token') || '';
  const tenantId = localStorage.getItem('tenant_id') || 'TENANT_001';

  const cloned = req.clone({
    setHeaders: {
      'Authorization': token ? `Bearer ${token}` : '',
      'X-Tenant-ID': tenantId
    }
  });

  return next(cloned);
};
